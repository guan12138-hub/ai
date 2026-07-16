import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCurrentUser, login as loginApi } from '@/api'

export const useUserStore = defineStore('user', () => {
    const user = ref(null)
    const token = ref(localStorage.getItem('token') || '')

    function setToken(t) {
        token.value = t
        localStorage.setItem('token', t)
    }

    async function login(credentials) {
        const t = await loginApi(credentials)
        setToken(t)
        user.value = await getCurrentUser()
        return user.value
    }

    function logout() {
        user.value = null
        token.value = ''
        localStorage.removeItem('token')
    }

    return { user, token, setToken, login, logout }
})
