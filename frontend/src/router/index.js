import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PostDetailView from '../views/PostDetailView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import MyPostsView from '../views/MyPostsView.vue'
import CreatePostView from '../views/CreatePostView.vue'
import EditPostView from '../views/EditPostView.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/post/:id',
    name: 'PostDetail',
    component: PostDetailView
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView
  },
  {
    path: '/my-posts',
    name: 'MyPosts',
    component: MyPostsView
  },
  {
    path: '/create-post',
    name: 'CreatePost',
    component: CreatePostView
  },
  {
    path: '/edit-post/:id',
    name: 'EditPost',
    component: EditPostView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
