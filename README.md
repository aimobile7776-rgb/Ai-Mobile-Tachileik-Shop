# AI Mobile Tachileik - Store Stock App

A dual-delivery digital catalog project for **AI Mobile** store in Tachileik, Myanmar. This project contains both a standalone native Android App and a fully responsive Web App (HTML/CSS/JS) for iOS users so everyone can check live stock.

---

## 📱 Standalone Android App (APK)

The Android app is built with **Kotlin** and **Jetpack Compose** using modern Material Design 3 guidelines.

### Core Android Features:
- **Premium Hero Banner & Scrolling Categories**: Highlights hot arrivals and easy navigation between iPhone/Android models.
- **Advanced Stock Filters**: Instantly filter stock by (iPhone New, iPhone Second, Android New, Android Second).
- **Responsive Products Grid**: Visual condition tags ("ပါကင်သစ်", "Second 99%") and pricing in THB.
- **Specs Detail Bottom Sheet**: Modern sheet with complete device specifications and single-tap Viber/Messenger social messaging actions.
- **Saved Favorites**: Powered by a robust **Room SQLite database** for offline local persistent tracking.
- **Functional Live Search**: Top-level search filters phone items dynamically as you type.

---

## 🌐 Responsive Web App (iOS / Safari / Desktop)

Located in the `/web` folder, this single-page application is fully responsive, mobile-first, and styled to exactly match the native Android App's aesthetics.

### Quick Start (How to deploy the Web App):
1. **Locate the `/web` directory**: Inside your exported ZIP project, find the `web/` folder.
2. **Deploy/Host**: Upload the contents of the `web/` folder to any static hosting provider (e.g., GitHub Pages, Netlify, Vercel, Firebase Hosting, or your own server).
3. **Open on iOS**: iOS users can open your deployed URL on Safari, tap "Share", and select **"Add to Home Screen"** to install it as a progressive-looking app wrapper!

---

## 🎨 Asset Customization (Store Logo Upload Slot)

You can customize the application launcher icon and header logo easily with your official brand logo:
1. Locate your store logo image (JPEG format is recommended).
2. Save it as **`img_store_logo.jpg`**.
3. Overwrite the file at **`app/src/main/res/drawable/img_store_logo.jpg`**.
4. Run/rebuild your project, and the Android launcher icon (3D adaptive setup) and Home Screen header logo will update automatically!

---

## 📞 Store Communication Channels

All communication channels can be configured in the project constants:
- **Viber Direct link**: `viber://chat?number=%2B959777666555`
- **Messenger URL**: `https://m.me/aimobile7776`
- **Physical Address**: No. 77, Bogyoke Road, Tachileik, Myanmar
