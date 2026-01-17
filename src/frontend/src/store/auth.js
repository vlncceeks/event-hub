import { ref, computed } from "vue";

const token = ref(localStorage.getItem("token"));
const user = ref(null);

const restoreUser = () => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
        user.value = JSON.parse(savedUser);
    }
};
restoreUser();

export function useAuth() {
    const isAuthenticated = computed(() => !!token.value);

    const isAdmin = computed(() => {
        return isAuthenticated.value && user.value?.role === "ADMIN";
    });

    const login = (newToken, userData) => {
        token.value = newToken;
        user.value = userData;

        localStorage.setItem("token", newToken);
        localStorage.setItem("user", JSON.stringify(userData));
    };

    const logout = () => {
        token.value = null;
        user.value = null;

        localStorage.removeItem("token");
        localStorage.removeItem("user");
    };

    const restore = () => {
        const savedUser = localStorage.getItem("user");
        if (savedUser) {
            user.value = JSON.parse(savedUser);
        }
    };

    restore();

    return {
        token,
        user,
        isAuthenticated,
        isAdmin,
        login,
        logout,
        restore,
    };
}
