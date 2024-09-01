<template>
  <div class="button-container">
    <button
        @click="toggleOptions"
        @mouseenter="hover = true"
        @mouseleave="hover = false"
        :class="{ 'hover': hover, 'active': isActive }"
        class="main-button"
    >
      메뉴
    </button>
    <transition-group name="slide-fade" tag="div" class="options-container">
      <button
          v-show="showOptions"
          key="practice"
          @click="selectOption('practice')"
          class="option-button practice"
      >
        연습모드
      </button>
      <button
          v-show="showOptions"
          key="exam"
          @click="selectOption('exam')"
          class="option-button exam"
      >
        시험모드
      </button>
    </transition-group>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hover: false,
      isActive: false,
      showOptions: false,
    };
  },
  methods: {
    toggleOptions() {
      this.isActive = !this.isActive;
      this.showOptions = !this.showOptions;
    },
    selectOption(option) {
      console.log(`Selected option: ${option}`);
      // 여기에 선택된 옵션에 대한 로직을 추가하세요
      this.showOptions = false;
      this.isActive = false;
    },
  },
};
</script>

<style scoped>
.button-container {
  position: fixed;
  display: inline-block;
  right: 20px;
  bottom: 100px;
}

.main-button {
  background-color: #ffd700; /* 노란색 달 색상 */
  color: #333;
  border: none;
  padding: 12px 24px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.main-button.hover {
  background-color: #ffe44d;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.main-button.active {
  background-color: #e6c200;
  transform: translateY(1px);
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
}

.options-container {
  position: absolute;
  bottom: calc(100% + 10px); /* 메인 버튼과의 간격 조정 */
  right: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 10; /* 옵션 버튼들이 메인 버튼 위에 나타나도록 함 */
}

.option-button {
  margin-bottom: 10px;
  padding: 8px 16px;
  font-size: 14px;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  opacity: 0;
  background-color: #fffae6; /* 옅은 노란색 배경 */
  color: #333;
}

.option-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background-color: #fff5cc;
}

.option-button:active {
  transform: translateY(1px);
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(20px);
  opacity: 0;
}

.slide-fade-enter-to,
.slide-fade-leave-from {
  transform: translateY(0);
  opacity: 1;
}
</style>