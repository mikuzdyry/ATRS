<template>
  <div id="app">
    <NavBar />
    <router-view />
    <ChatWidget />
  </div>
</template>

<script setup>
import NavBar from './components/NavBar.vue'
import ChatWidget from './components/ChatWidget.vue'
</script>
