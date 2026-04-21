# FoodTime - Minecraft Fabric Mod (1.20.1)

FoodTime 是一個專為 Minecraft Fabric 1.20.1 (至 1.20.4) 設計的 Client-side 模組 (客戶端模組)。這個模組會在玩家食用帶有「時間簡介 (Lore/Tooltip)」的自定義食物時，自動解析該時間，並在遊戲畫面左方顯示一個倒數計時 HUD。

## 🌟 主要功能 (Features)

- **自動解析食物時間**：只要食物的簡介 (Tooltip) 中帶有時間格式（例如 `10:00`、`5m` 等），吃下後就會自動開始倒數。
- **離線時間計算**：倒數計時是基於現實世界的 Timestamp。即使你退出單人世界或伺服器，時間依然會在背景流逝，重登後剩餘時間會自動校正！
- **多行清單顯示**：支援同時吃下多種帶有時間的食物，HUD 會以第一排、第二排的列表方式整齊顯示所有倒數項目。
- **忽略普通食物**：吃下原版食物（如烤牛肉、麵包）或沒有時間說明的食物時，系統會自動忽略，不會顯示多餘的錯誤資訊。
- **完全自訂的 HUD (內建編輯器)**：
  - 支援從 **Mod Menu** 進入設定介面。
  - 提供 **快捷鍵支援 (預設為 `O` 開啟設定，`P` 開啟編輯模式)**。
  - 在編輯模式中，你可以**直接用滑鼠拖曳**來移動 HUD 的位置，並**使用滑鼠滾輪**調整 HUD 的大小縮放。

## ⚙️ 前置需求 (Requirements)

安裝此模組前，請確保你的 Minecraft 客戶端已安裝以下依賴：

* Minecraft 1.20.1 - 1.20.4
* [Fabric Loader](https://fabricmc.net/) (建議版本 0.15.11 或以上)
* [Fabric API](https://modrinth.com/mod/fabric-api)
* [Cloth Config API](https://modrinth.com/mod/cloth-config) (用於設定選單)
* *(選用推薦)* [Mod Menu](https://modrinth.com/mod/modmenu) (方便從遊戲選單管理模組)

## 🎮 如何使用 (Usage)

1. 將下載編譯好的 `FoodTime-1.0.0.jar` 放入你的 `.minecraft/mods` 資料夾。
2. 啟動遊戲。
3. 進入遊戲後，你可以按下快捷鍵 **`O`** 來打開一般設定選單（開啟/關閉 HUD）。
4. 按下快捷鍵 **`P`** 進入 **HUD 編輯模式**。在編輯模式中：
   * **按住滑鼠左鍵**：可以隨意拖曳 HUD 的位置。
   * **滾動滑鼠滾輪**：可以放大或縮小 HUD 的尺寸。
   * **按下 ESC**：儲存設定並退出編輯模式。

## 🛠️ 開發與編譯 (Building from source)

如果你想要自行編譯這個模組，請先確保你的電腦已安裝 Java 17。
將本專案複製 (Clone) 到本地後，在終端機執行以下指令：

```bash
# Windows
./gradlew clean build

# Mac / Linux
./gradlew clean build
```

編譯完成的 `.jar` 檔案會產生在 `build/libs/` 資料夾內。

## 📄 授權 (License)

本模組採用 [MIT License](LICENSE) 開源授權。
