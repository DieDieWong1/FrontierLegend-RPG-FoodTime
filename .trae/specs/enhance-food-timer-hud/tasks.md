# Tasks
- [x] Task 1: 實作設定介面快捷鍵 (KeyBinding)
  - [x] SubTask 1.1: 在 Client 啟動時註冊一個新的 KeyBinding (預設按鍵可設為 `UNKNOWN` 或 `O` 鍵等)，並分類至該模組名稱下。
  - [x] SubTask 1.2: 在 `ClientTickEvents.END_CLIENT_TICK` 中監聽該按鍵的 `wasPressed()` 事件。
  - [x] SubTask 1.3: 當按鍵被按下時，呼叫開啟 Cloth Config 介面的方法 (`MinecraftClient.getInstance().setScreen(...)`)。
- [x] Task 2: 實作多行食物倒數顯示
  - [x] SubTask 2.1: 確保 `FoodTimerManager` 內的資料結構為 `List<TimerEntry>`，並確保 JSON 存取支援 List 序列化。
  - [x] SubTask 2.2: 當玩家吃下新的有效食物時，將其 `add` 到 List 中 (如果已存在同名效果，可選擇覆蓋或獨立新增，這裡預設為獨立新增或依需求刷新)。
  - [x] SubTask 2.3: 修改 `FoodTimerHudRenderer`，使用迴圈遍歷清單，每畫完一項就將 Y 座標往下偏移 (例如 `y + index * 12`)，以達成第一排、第二排的效果。
- [x] Task 3: 確保忽略無時間的食物
  - [x] SubTask 3.1: 在 `LivingEntityMixin` 的攔截點中，檢查 `FoodTooltipParser` 傳回的秒數是否大於 0。
  - [x] SubTask 3.2: 只有在秒數大於 0 的情況下，才呼叫 `FoodTimerManager` 新增倒數計時。

# Task Dependencies
- [Task 1] depends on nothing
- [Task 2] depends on nothing
- [Task 3] depends on nothing
