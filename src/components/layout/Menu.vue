<template>
    <div class="sidebar">
        <nav class="sidebar-nav">
            <ul class="nav">
                <router-link :to="'/guestbook/dashboard'" class="nav-item">
                    <a v-on:click="handleClickMenu('Dashboard')" class="nav-link"><i class="icon-speedometer"></i> Guestbook</a>
                </router-link>
                <li class="divider"></li>
                <li class="nav-title">
                    Navigator
                </li>
                <router-link :to="'/profile/update'" class="nav-item">
                    <a v-on:click="handleClickMenu('Profile')" class="nav-link"><i class="icon-pencil"></i> Profile</a>
                </router-link>
                <router-link :to="'/testimonial/list'" class="nav-item">
                    <a v-on:click="handleClickMenu('Testimonial')" class="nav-link"><i class="icon-pencil"></i> Testimonial</a>
                </router-link>
                <router-link tag="li" class="nav-item nav-dropdown" :to="{ path: '/menu'}" disabled>
                    <div class="nav-link nav-dropdown-toggle" @click="handleClickParent">
                        <i class="icon-note"></i> Item
                    </div>
                    <ul class="nav-dropdown-items">
                        <li class="nav-item" v-on:click="handleClickMenu('New Item')">
                            <router-link :to="'/item/new'" class="nav-link" style="padding-left : 30px;">
                                <i class="icon-pencil"></i> New
                            </router-link>
                        </li>
                        <li class="nav-item" v-on:click="handleClickMenu('List Item')">
                            <router-link :to="'/item/list'" class="nav-link" style="padding-left : 30px;">
                                <i class="icon-pencil"></i> List
                            </router-link>
                        </li>
                    </ul>
                </router-link>
            </ul>
        </nav>
    </div>
</template>
<script>
export default {
    contentData () {
        return {

        }
    },
    methods: {
        handleClickParent (e) {
            e.preventDefault()
            e.target.parentElement.classList.toggle('open')
        },
        handleClickMenu (event) {
          this.$emit('menuSelected', event)
      }
  },
}
</script>

<style scoped>

</style>
