<template>
  <div>
    <router-view></router-view>
  </div>


</template>

<script>

export default {
  name: 'App',
  // components: {
  //
  // }
}
</script>

<style>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans KR', sans-serif;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0px;
}

:root {
  --secondary-blue: black; /* 스틸블루 */
  --background-light: #FFFAF0; /* 플로랄 화이트 */
  --text-dark: #333333;
  --text-light: #FFFFFF;
  --hover-yellow: #FFC000; /* 더 진한 노란색 */
}
a{
  text-decoration: none;
  color: inherit;
}

</style>
