# Tasks
- [x] Task 1: 建立 Fabric 1.20.1 - 1.20.4 Client 模組專案
  - [x] SubTask 1.1: 設定 `gradle.properties` 與 `fabric.mod.json` 以支援對應 Minecraft 版本，標記為 Client-only 模組。
  - [x] SubTask 1.2: 建立主類別並實作 `ClientModInitializer`。
- [x] Task 2: 實作食物簡介解析與食用事件監聽
  - [x] SubTask 2.1: 透過 Mixin 注入 `LivingEntity#eatFood` 或使用 Fabric API 事件，攔截玩家吃食物的動作。
  - [x] SubTask 2.2: 實作解析邏輯，從物品 NBT (Tooltip/Lore) 中提取自定義名稱，並使用正則表達式解析時間格式轉換為總秒數。
- [x] Task 3: 實作時間記錄與狀態持久化
  - [x] SubTask 3.1: 在 Client 端建立資料結構，儲存 `食物名稱`、`起始時間 (Timestamp)` 與 `總倒數時間`。
  - [x] SubTask 3.2: 將此資料結構保存至本地 JSON 檔案中，在遊戲啟動或登入時讀取，並在更新時寫入，確保離線計算的準確性。
- [x] Task 4: 實作 HUD 渲染與倒數計算
  - [x] SubTask 4.1: 註冊 `HudRenderCallback`，在遊戲畫面繪製文字。
  - [x] SubTask 4.2: 每一幀根據當前時間與起始時間的差值計算剩餘時間。如果小於等於 0 則從清單移除。
  - [x] SubTask 4.3: 將文字繪製在預設中左方位置。
- [x] Task 5: 實作設定選單 (Config UI)
  - [x] SubTask 5.1: 整合配置函式庫 (如 Cloth Config API)，建立設定檔類別。
  - [x] SubTask 5.2: 增加設定項目：開啟/關閉 HUD (布林值)、X 座標 (整數)、Y 座標 (整數)、縮放大小 (浮點數)。
  - [x] SubTask 5.3: 將設定檔的值套用至 Task 4 的 HUD 渲染邏輯中。
  - [x] SubTask 5.4: 確保設定介面可從 Mod Menu 開啟。

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 2]
- [Task 4] depends on [Task 3]
- [Task 5] depends on [Task 4]
