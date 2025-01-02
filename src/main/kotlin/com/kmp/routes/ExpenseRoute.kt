package com.kmp.routes

import com.kmp.data.model.Expense
import com.kmp.data.model.MessageResponse
import com.kmp.data.model.expenses
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Route.expensesRouting(){

    get("/expenses"){
        //Vid 67
        call.respond(status = HttpStatusCode.OK, expenses)
    }

    //Vid 57

    get("/expenses/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val expense = expenses.find { it.id == id }
        if(id == null || expense == null) {
            call.respond(HttpStatusCode.NotFound, MessageResponse("Expense not found"))
            return@get
        }
        call.respond(HttpStatusCode.OK, expense)
    }

    post("/expenses") {
        val expense = call.receive<Expense>()
        //Vid 67
        val lastExpenseId = if (expenses.isEmpty()) {
            expense.id
        } else {
            //Vid 61, max 1 Otherwise, find the maximum ID and increment it
            expenses.maxOf { it.id } + 1
        }
        expenses.add(expense.copy(id = lastExpenseId))
        call.respond(HttpStatusCode.OK, MessageResponse("Expense added successfully"))
    }

    //Vid 58,para editar
    put("expenses/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val expense = call.receive<Expense>()
        if(id == null || id !in 0 until expenses.size) {
            call.respond(HttpStatusCode.NotFound, MessageResponse("Expense not found"))
            return@put
        }
        val index = expenses.indexOfFirst { it.id == id }
        expenses[index] = expense.copy(id = id)
        call.respond(HttpStatusCode.OK, MessageResponse("Expense updated successfully"))
    }
    //Vid 59
    delete("/expenses/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        //Vid 67
        val expense = expenses.find { it.id == id }
        //Vid 67
        if(id == null || expense == null) {
            call.respond(HttpStatusCode.NotFound, MessageResponse("Expense not found"))
            return@delete
        }
        expenses.removeIf { it.id == id }
        call.respond(HttpStatusCode.OK, MessageResponse("Expense removed successfully"))
    }




}