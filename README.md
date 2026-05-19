# Lariel’s Quality of Life – Pixelmon QoL Mod

A modern Quality‑of‑Life mod for **Pixelmon Reforged / NeoForge**, designed to enhance gameplay with custom GUIs, improved breeding mechanics, new items, and various comfort features.  
The goal is to streamline everyday Pixelmon interactions without altering the core experience.

---

## ✨ Features

## 🎯 Pokémon Spawn Tracker

The mod includes an optional **Pokémon Spawn Tracker** that alerts the player when special Pokémon appear in the world and provides an interactive way to track them.

### ✨ What It Detects
The tracker listens for important Pixelmon spawns, including:

- Shiny Pokémon
- Boss Pokémon
- Legendary Pokémon
- Ultra Beasts
- Any other special‑flagged Pixelmon

When such a Pokémon spawns near the player, a **system message** is displayed.

### 📨 Interactive Notifications
The notification includes:

- The Pokémon’s display name
- Its approximate location
- A **clickable coordinate link**

Clicking the link automatically starts tracking the Pokémon.

### 🧭 Entity Tracking System
Once tracking is activated:

- The mod locates the target entity by UUID
- A directional indicator (arrow) is shown on screen
- The arrow updates every tick and always points toward the target
- Tracking automatically stops if the entity despawns or is defeated

This system allows players to quickly locate rare or valuable Pokémon without relying on teleport commands or external tools.

### 🛠 Technical Notes
- Uses Pixelmon’s spawn events to detect special Pokémon
- Tracking is handled server‑side and updated per tick
- Directional arrows are rendered client‑side using lightweight UI overlays
- No mixins or invasive patches required
- Fully compatible with NeoForge 20.4+ and Pixelmon 9.2–9.3

## 🧪 Mint Trader (Custom GUI)
A fully custom trading interface for Pokémon Mints.

- Custom Screen + Menu
- Dropdown selection for desired Mint
- Server‑side validation
- GUI‑based success/error messages (no chat spam)
- Easily extendable for additional currencies or logic

## 🥚 Better Breeding
Improves the Pokémon breeding experience with clearer logic and better control.

- Form and palette handling
- Control gender
- Shiny options - can be also disabled
- Own GUI for editing Eggs (Form/Palette/Gender)
- Clean inheritance logic
- Extendable service structure for future breeding features

## 📈 EV Items & QoL Mechanics
Adds new items and mechanics to simplify EV training and Pokémon management.

- Configurable EV increases
- Dynamic tooltips using translation keys
- Seamless integration with Pixelmon systems

## 🧩 Clean Architecture
The mod is structured for clarity and maintainability:

- `menu` → server‑side container logic
- `client/screen` → GUI rendering
- `network` → custom packets
- `services` → internal logic
- `registry` → menu/screen registrations

---

## 🛠 Technical Overview

### 🔌 Networking
Uses NeoForge’s modern `StreamCodec` system:

- Client → Server packets for actions
- Server → Client packets for GUI messages
- Clean separation of responsibilities
- No legacy Forge networking

---

## 📦 Project Structure

```text
de.lariel.qualityoflife
├── menu
│    ├── MintTraderMenu.java
│    ├── MintTraderMenuProvider.java
│    └── registry
│         └── LarielsQoLModMenus.java
│
├── client
│    └── screen
│         ├── LarielMintTraderScreen.java
│         └── registry
│              └── LarielsQoLModScreens.java
│
├── network
│    ├── packet
│    ├── packet.base
│    └── LarielNetwork.java
│
├── service
└── util
```

---

## 📥 Installation

1. Install **NeoForge**
2. Install **Pixelmon Reforged**
3. Place this mod into your `mods/` folder
4. Launch the game

---

## 🧩 Compatibility

- Minecraft **1.21.x**
- NeoForge **20.4+**
- Pixelmon Reforged **9.2–9.3**

---

## 🤝 Contributing

Pull Requests are welcome.  
Please follow these guidelines:

- Maintain clean package structure
- Keep client/server logic separated
- Avoid unnecessary Mixins
- Use meaningful commit messages

---

## 🧭 Roadmap

- Additional QoL GUIs
- Custom shop system
- Scoreboard‑based currency support
- Expanded breeding features
- Target Dummy
- Invincible-Ribbon -> if a Pokémon get's lvl 100 without beeing defeated
- Custom Armor-set-bonus