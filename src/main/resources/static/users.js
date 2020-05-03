var usuarios = [];

function findUser(userId) {
    return usuarios[findProductKey(userId)];
}

function findProductKey(userId) {
    for (var key = 0; key < usuarios.length; key++) {
        if (usuarios[key].id == userId) {
            return key;
        }
    }
}

var usuarioServImplement = {
    findAll(fn) {
        axios
            .get('/api/v1/users')
            .then(response => fn(response))
            .catch(error => console.log(error))
    },

    findById(id, fn) {
        axios
            .get('/api/v1/users/' + id)
            .then(response => fn(response))
            .catch(error => console.log(error))
    },

    create(user, fn) {
        axios
            .post('/api/v1/users', user)
            .then(response => fn(response))
            .catch(error => console.log(error))
    },

    deleteUser(id, fn) {
        axios
            .delete('/api/v1/users/' + id)
            .then(response => fn(response))
            .catch(error => console.log(error))
    },
    update(id, user, fn) {
        axios
          .put('/api/v1/users/' + id, user)
          .then(response => fn(response))
          .catch(error => console.log(error))
      }

}
var List = Vue.extend({
    template: '#user-list',
    data: function () {
        return { usuarios: [], searchKey: '' };
    },
    computed: {
        filteredProducts() {
            return this.usuarios.filter((user) => {
                return user.nombre.indexOf(this.searchKey) > -1
                    || user.apellido.indexOf(this.searchKey) > -1

            })
        }
    },
    mounted() {
        usuarioServImplement.findAll(r => { this.usuarios = r.data; usuarios = r.data })
    }
});

var User = Vue.extend({
    template: '#user',
    data: function () {
        return { user: findUser(this.$route.params.user_id) };
    }
});

var UserEdit = Vue.extend({
    template: '#user-edit',
    data: function () {
        return { user: findUser(this.$route.params.user_id) };
    },
    methods: {
        updateUser: function () {
        	usuarioServImplement.update(this.user.id, this.user, r => router.push('/'))
        }
    }
});

var UserDelete = Vue.extend({
    template: '#user-delete',
    data: function () {
        return { user: findUser(this.$route.params.user_id) };
    },
    methods: {
    	deleteUser: function () {
            usuarioServImplement.deleteUser(this.user.id, r => router.push('/'))
        }
    }
});

var AddUser = Vue.extend({
    template: '#add-user',
    data() {
        return {
            user: { nombre: '', apellido: '', correo: '', edad: 0 }
        }
    },
    methods: {
        createUser() {
            usuarioServImplement.create(this.user, r => router.push('/'))
        }
    }
});

var router = new VueRouter({
    routes: [
        { path: '/', component: List },
        { path: '/user/:user_id', component: User, name: 'user' },
        { path: '/add-user', component: AddUser },
        { path: '/user/:user_id/edit', component: UserEdit, name: 'user-edit' },
        { path: '/user/:user_id/delete', component: UserDelete, name: 'user-delete' }
    ]
});

new Vue({
    router
}).$mount('#app')