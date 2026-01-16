<template>
  <Header></Header>
  <AuthHeader></AuthHeader>
  <UsersList></UsersList>
  <EventList></EventList>
</template>

<script>
import UsersList from "@/components/UsersList.vue";
import AuthHeader from "@/components/AuthHeader.vue";
import EventList  from "@/components/EventList.vue";
import Header from "@/components/AppHeader.vue";

export default {
  name: 'App',
  components: {
    UsersList,
    AuthHeader,
    EventList,
    Header
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
