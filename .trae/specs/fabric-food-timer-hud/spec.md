# Food Timer HUD Spec

## Why
玩家在 Minecraft 中食用具有自定義效果時間的食物時，需要一個能直觀看到剩餘時間的 HUD 介面。並且因為這些效果可能在玩家離線時也會繼續流逝，所以需要根據現實時間計算剩餘時間。

## What Changes
- 建立一個適用於 Fabric 1.20.1 - 1.20.4 的 Client 端模組 (Client-side Mod)。
- 攔截玩家食用食物的事件，解析食物簡介 (Lore/Tooltip) 中標示的時間，並記錄當下現實時間及食物名稱。
- 在遊戲畫面 (HUD) 中左方顯示正在倒數的食物名稱 (包含自定義名稱) 和剩餘時間。
- 即使玩家登出單人世界或伺服器，時間倒數仍會在背景根據現實時間繼續計算。
- 新增設定選單 (Config UI)，允許玩家開啟/關閉此 HUD 功能，並可隨意調整其大小與在螢幕上的位置。

## Impact
- Affected specs: 玩家 HUD 顯示 (新增渲染層)、客戶端設定檔系統。
- Affected code:
  - 食用物品事件監聽器 (Item Usage Callback / Mixin)
  - 遊戲畫面渲染器 (HudRenderCallback)
  - 客戶端資料儲存系統 (儲存食用時間點及食物資料)
  - 設定選單介面 (例如整合 Cloth Config API 或是 MidnightLib)

## ADDED Requirements
### Requirement: 食物時間解析與記錄
系統必須能在玩家食用食物後，自動抓取該物品的名稱與簡介中的時間。

#### Scenario: 玩家吃下帶有時間簡介的食物
- **WHEN** 玩家吃下食物，且該食物簡介中包含時間格式 (例如 "10:00" 或 "10分鐘")
- **THEN** 系統記錄當前真實世界時間 (Timestamp) 作為起點，並保存食物名稱及總倒數時間。

### Requirement: 離線時間流逝與 HUD 顯示
系統必須正確計算離線流逝的時間，並在畫面上顯示。

#### Scenario: 玩家重新進入遊戲並查看 HUD
- **WHEN** 玩家進入單人世界或伺服器，且記錄中的倒數時間尚未結束
- **THEN** 根據記錄的起點時間與當前真實時間，計算出準確的剩餘時間，並在螢幕中左方顯示 "食物名稱: 剩餘時間"。

### Requirement: HUD 設定與自訂
系統必須提供設定介面讓玩家自訂 HUD 外觀。

#### Scenario: 玩家開啟模組設定
- **WHEN** 玩家進入模組設定選單 (Mod Menu / Config UI)
- **THEN** 玩家可以切換開啟或關閉該 HUD 顯示，並可透過滑桿或輸入框調整 HUD 的 X 座標、Y 座標及縮放比例 (Scale)。
