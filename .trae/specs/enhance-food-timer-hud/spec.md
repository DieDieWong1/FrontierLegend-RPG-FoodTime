# Enhance Food Timer HUD Spec

## Why
針對目前模組的功能，玩家提出三個問題與需求：
1. 期望能夠有獨立的快捷鍵直接開啟設定介面，並且可以在控制選單中修改這個按鍵。
2. 期望在吃下多個有時間倒數的食物時，能夠同時顯示（例如：第一排第一個，第二排第二個）。
3. 確認吃下「沒有倒數時間簡介」的普通食物時不會顯示，這點需要明確在邏輯中防呆。

## What Changes
- 新增一組可自訂的快捷鍵 (KeyBinding)，按下後能直接打開設定 UI (Config UI)。
- 修改 HUD 渲染與資料管理邏輯，從單一倒數顯示改為「列表 (List)」式的多行顯示，讓多個食物的倒數時間可以同時存在。
- 在食用事件的攔截邏輯中加入嚴格的檢查，確保只有解析出有效時間 (大於 0 秒) 的食物兩會被加入倒數清單。

## Impact
- Affected specs: 玩家 HUD 顯示 (支援多行排版繪製)、按鍵綁定 (新增 KeyBinding)。
- Affected code:
  - `FoodTimerHudRenderer` (修改為迴圈遍歷清單並計算 Y 軸偏移量)
  - `FoodTimerManager` (確保資料結構是 List 而不是單一物件)
  - `FoodTooltipParser` / `LivingEntityMixin` (防護沒有時間的食物)
  - 新增 KeyBinding 註冊與 Client Tick 監聽器。

## ADDED Requirements
### Requirement: 快捷鍵開啟設定 UI
系統必須提供一個按鍵，讓玩家不需透過 Mod Menu 也能直接打開設定畫面。

#### Scenario: 玩家按下自訂快捷鍵
- **WHEN** 玩家在遊戲中按下預設的快捷鍵 (例如預設為未綁定或某個字母鍵)
- **THEN** 系統會直接打開 Cloth Config 產生的設定介面，玩家也可以在原版「控制」選單中修改此按鍵。

### Requirement: 多個食物的倒數顯示
系統必須支援同時追蹤並顯示多個食物的剩餘時間。

#### Scenario: 玩家連續吃下多個有時間的食物
- **WHEN** 玩家先吃下第一個有時間的食物，接著又吃下第二個有時間的食物
- **THEN** HUD 中左方第一排顯示第一個食物的倒數，第二排顯示第二個食物的倒數，且各自獨立計算時間。

## MODIFIED Requirements
### Requirement: 略過無時間的食物
- **WHEN** 玩家吃下普通的食物 (如原版烤牛肉，且沒有任何時間簡介)
- **THEN** 系統解析後判斷為無效時間 (0 秒或 null)，不將其加入倒數清單，畫面上不會有任何新增顯示。
