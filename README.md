# StressMeter

The fun way to track your daily stress levels

## 📱 Overview

StressMeter is a modern Android application built to help users track and visualize their stress levels over time. Using a unique image-based input system, users can quickly log their stress levels through intuitive visual representations, making the process engaging and user-friendly.

### Key Features

- 🖼️ **Visual Stress Input**: Select from a grid of images that best represents your current stress level
- 📊 **Data Visualization**: View your stress patterns through interactive line charts
- 📅 **Historical Data**: Track your stress levels over time with detailed timestamps
- 📱 **Modern UI**: Material Design with intuitive navigation drawer
- 📈 **Analytics**: View comprehensive statistics about your stress patterns
- 🔄 **Data Management**: Easy data clearing and management options

## 🛠️ Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Components**:
  - AndroidX
  - Material Design Components
  - MPAndroidChart for data visualization
  - RecyclerView for efficient list rendering
- **Concurrency**: Coroutines for async operations
- **Navigation**: Navigation Component with NavDrawer
- **Data Storage**: Local CSV file storage
- **Build System**: Gradle with Kotlin DSL

## 🏗️ Architecture

The app follows clean architecture principles with a clear separation of concerns:

- **UI Layer**: Fragments and Activities
- **ViewModel Layer**: Business logic and UI state management
- **Data Layer**: Local storage handling