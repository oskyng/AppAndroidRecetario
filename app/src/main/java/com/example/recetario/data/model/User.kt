package com.example.recetario.data.model

import java.util.UUID

open class User(
    open val id: String,
    open val firstname: String,
    open val lastname: String,
    open val email: String,
    open val username: String,
    open var password: String
)

data class RegularUser(
    override val id: String = UUID.randomUUID().toString(),
    override val firstname: String,
    override val lastname: String,
    override val email: String,
    override val username: String,
    override var password: String,
    val receiveNewsletter: Boolean = false
) : User(id, firstname, lastname, email, username, password) {
    fun subscribeToNewsletter(subscribe: Boolean): RegularUser {
        return copy(receiveNewsletter = subscribe)
    }
}

data class ChefUser(
    override val id: String = UUID.randomUUID().toString(),
    override val firstname: String,
    override val lastname: String,
    override val email: String,
    override val username: String,
    override var password: String,
    val createdRecipes: MutableList<Recipe> = mutableListOf()
) : User(id, firstname, lastname, email, username, password) {
    fun addCreatedRecipe(recipe: Recipe): ChefUser {
        createdRecipes.add(recipe)
        return this
    }
}