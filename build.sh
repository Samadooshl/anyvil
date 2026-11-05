#!/bin/bash

# Anyvil - Build and Install Script
# Mobile-first IDE for Android

set -e

PROJECT_DIR="/root/anyvil"
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
PACKAGE_NAME="io.github.samadooshl.anyvil"

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo_success() { echo -e "${GREEN}✓ $1${NC}"; }
echo_info() { echo -e "${YELLOW}ℹ $1${NC}"; }
echo_error() { echo -e "${RED}✗ $1${NC}"; }

cd "$PROJECT_DIR"

echo ""
echo "🚀 Anyvil - Mobile-first IDE"
echo "============================="
echo ""

# Check prerequisites
echo_info "Checking prerequisites..."
if ! command -v adb &> /dev/null; then
    echo_error "ADB not found. Install Android SDK Platform Tools."
    exit 1
fi
echo_success "Prerequisites OK"

# Make gradlew executable
chmod +x gradlew 2>/dev/null || true

# Clean
echo_info "Cleaning project..."
./gradlew clean

# Build
echo_info "Building APK..."
./gradlew assembleDebug

if [ -f "$APK_PATH" ]; then
    echo_success "Build complete: $APK_PATH"
    ls -lh "$APK_PATH"
    echo ""

    # Check device
    if ! adb devices | grep -q "device$"; then
        echo_error "No device connected. Enable USB debugging."
        exit 1
    fi

    # Uninstall old version
    echo_info "Uninstalling old version..."
    adb uninstall "$PACKAGE_NAME" 2>/dev/null || echo "   (no previous version)"

    # Install
    echo_info "Installing new version..."
    adb install "$APK_PATH"

    echo ""
    echo_success "Installation complete!"
    echo ""
    echo "📱 Anyvil Features to Test:"
    echo "   • Bottom-anchored action bar"
    echo "   • Keyboard helper row (⌨ button)"
    echo "   • Multi-terminal tabs (▷ button)"
    echo "   • File browser (📁 button)"
    echo "   • Dark-first amber accent theme"
    echo "   • 48dp+ touch targets"
    echo ""
else
    echo_error "Build failed - APK not found"
    exit 1
fi