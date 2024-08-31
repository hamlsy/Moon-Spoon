<template>
  <main>
  <div class="floating-action-button" >
    <button @click="toggleMenu" class="main-button" :class="{ 'active': isOpen }">
      <i class="fas fa-vial"></i>
    </button>
    <transition-group name="fade-slide" tag="div" class="sub-buttons" >
      <button v-if="isOpen" key="practice" class="sub-button practice" @click.native="selectOption('practice')">
        <i class="fas fa-dumbbell"></i>
        <p class="btn-text">연습모드</p>
      </button>
      <button v-if="isOpen" key="test" class="sub-button test" @click.native="selectOption('test')">
        <i class="fas fa-clipboard-check"></i>
        <p class="btn-text">테스트</p>
      </button>
    </transition-group>
  </div>
  </main>
</template>

<script>
export default {
  data() {
    return {
      isOpen: false
    }
  },
  methods: {
    toggleMenu() {
      this.isOpen = !this.isOpen
    },
    selectOption(option) {
      console.log(`Selected option: ${option}`)
      this.isOpen = false
    },
  }
}
</script>

<style scoped>
.floating-action-button {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  display: flex;
  flex-direction: column-reverse;
  align-items: flex-end;
}

.main-button, .sub-button {
  width: 130px;
  height: 80px;
  border-radius: 20px;
  border: none;
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
  display: flex;
  justify-content: center;
  align-items: center;

}

.main-button {
  background-color: #6200ea;
  z-index: 2;
}

.main-button:hover {
  background-color: #7c4dff;
  transform: scale(1.1);
}

.main-button.active {
  transform: rotate(5deg);
}

.sub-button {
  margin-bottom: 1rem;
  opacity: 1;
  transform: translateY(10px);
}

.sub-button.practice {
  background-color: #00c853;
}

.sub-button.test {
  background-color: #ff6d00;
}

.sub-button:hover {
  transform: scale(1.1);
}

.fade-slide-enter-active, .fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter, .fade-slide-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

i {
  margin-right: 5px;

}

.btn-text{
  font-size: 1rem;
}
</style>