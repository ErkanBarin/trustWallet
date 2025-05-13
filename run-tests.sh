#!/bin/bash

# Function to display usage
usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  -m, --mode <local|browserstack>  Test mode (default: local)"
    echo "  -d, --device <device_name>       Device name for local testing"
    echo "  -v, --version <platform_version> Platform version for local testing"
    echo "  -u, --udid <device_udid>         Device UDID for local testing"
    echo "  -h, --help                       Display this help message"
    exit 1
}

# Default values
MODE="local"
DEVICE_NAME="Android Device"
PLATFORM_VERSION="11.0"
UDID=""

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        -m|--mode)
            MODE="$2"
            shift
            shift
            ;;
        -d|--device)
            DEVICE_NAME="$2"
            shift
            shift
            ;;
        -v|--version)
            PLATFORM_VERSION="$2"
            shift
            shift
            ;;
        -u|--udid)
            UDID="$2"
            shift
            shift
            ;;
        -h|--help)
            usage
            ;;
        *)
            echo "Unknown option: $1"
            usage
            ;;
    esac
done

# Update config.properties
update_config() {
    sed -i '' "s/test.mode=.*/test.mode=$MODE/" src/main/java/com/trustwallet/config/config.properties
}

# Update testng.xml
update_testng() {
    if [ "$MODE" = "local" ]; then
        sed -i '' 's/Create Wallet Test - Local" enabled="false"/Create Wallet Test - Local" enabled="true"/' testng.xml
        sed -i '' 's/Create Wallet Test - BrowserStack" enabled="true"/Create Wallet Test - BrowserStack" enabled="false"/' testng.xml
    else
        sed -i '' 's/Create Wallet Test - Local" enabled="true"/Create Wallet Test - Local" enabled="false"/' testng.xml
        sed -i '' 's/Create Wallet Test - BrowserStack" enabled="false"/Create Wallet Test - BrowserStack" enabled="true"/' testng.xml
    fi
}

# Set up Android SDK environment
setup_android_sdk() {
    if [ "$MODE" = "local" ]; then
        echo "Setting up Android SDK environment..."
        
        # Create Android SDK directory if it doesn't exist
        mkdir -p ~/Library/Android/sdk
        
        # Download Android SDK command line tools if they don't exist
        if [ ! -d ~/Library/Android/sdk/cmdline-tools ]; then
            echo "Downloading Android SDK command line tools..."
            TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-mac-9477386_latest.zip"
            TEMP_DIR=$(mktemp -d)
            curl -L -o "$TEMP_DIR/tools.zip" "$TOOLS_URL"
            mkdir -p ~/Library/Android/sdk/cmdline-tools/latest
            unzip -q "$TEMP_DIR/tools.zip" -d "$TEMP_DIR"
            mv "$TEMP_DIR/cmdline-tools"/* ~/Library/Android/sdk/cmdline-tools/latest/
            rm -rf "$TEMP_DIR"
        fi
        
        # Set environment variables
        export ANDROID_HOME=~/Library/Android/sdk
        export ANDROID_SDK_ROOT=~/Library/Android/sdk
        export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
        
        # Install platform-tools if they don't exist
        if [ ! -d ~/Library/Android/sdk/platform-tools ]; then
            echo "Installing Android SDK platform-tools..."
            yes | ~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager "platform-tools"
        fi
        
        echo "Android SDK environment set up successfully."
    fi
}

# Install Appium drivers if needed
install_drivers() {
    if [ "$MODE" = "local" ]; then
        echo "Checking and installing required Appium drivers..."
        APPIUM_PATH=~/.asdf/installs/nodejs/20.17.0/bin/appium
        
        # Check if UiAutomator2 driver is installed
        if ! $APPIUM_PATH driver list --installed | grep -q "uiautomator2"; then
            echo "Installing UiAutomator2 driver..."
            $APPIUM_PATH driver install uiautomator2
        else
            echo "UiAutomator2 driver is already installed."
        fi
    fi
}

# Start Appium server if running locally
start_appium() {
    if [ "$MODE" = "local" ]; then
        echo "Starting Appium server..."
        # Kill any existing Appium processes
        pkill -f "appium" || true
        sleep 2
        
        # Start Appium with the correct path and environment variables
        ANDROID_HOME=~/Library/Android/sdk \
        ANDROID_SDK_ROOT=~/Library/Android/sdk \
        PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools \
        ~/.asdf/installs/nodejs/20.17.0/bin/appium -p 4725 -a 127.0.0.1 -pa /wd/hub &
        APPIUM_PID=$!
        echo "Appium server started with PID: $APPIUM_PID"
        sleep 5
    fi
}

# Stop Appium server if running locally
stop_appium() {
    if [ "$MODE" = "local" ]; then
        echo "Stopping Appium server..."
        pkill -f "appium" || true
        echo "Appium server stopped"
    fi
}

# Run tests
run_tests() {
    echo "Running tests in $MODE mode..."
    if [ "$MODE" = "local" ]; then
        ANDROID_HOME=~/Library/Android/sdk \
        ANDROID_SDK_ROOT=~/Library/Android/sdk \
        PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools \
        mvn clean test -DdeviceName="$DEVICE_NAME" -DplatformVersion="$PLATFORM_VERSION" -Dudid="$UDID"
    else
        mvn clean test
    fi
}

# Main execution
echo "Setting up test environment for $MODE mode..."
update_config
update_testng
setup_android_sdk
install_drivers
start_appium

# Run tests and capture exit code
run_tests
EXIT_CODE=$?

# Clean up
stop_appium

exit $EXIT_CODE 