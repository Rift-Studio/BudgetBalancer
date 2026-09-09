package riftappstudios.finance.budgetbalancer.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import riftappstudios.finance.budgetbalancer.data.objects.Budgeting
import riftappstudios.finance.budgetbalancer.data.objects.Categories
import riftappstudios.finance.budgetbalancer.data.objects.Transactions

class BudgetService(
    val client: HttpClient
) : IBudgetService {

    override suspend fun refresh(): StateResponse<Boolean> {
        return try {
            // Make a GET request
            val response: HttpResponse = client.get("http://localhost:5000/append-new-transactions")
//            println("Status: ${response.status} - $data")
            StateResponse.Success(true)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            StateResponse.Error(e)
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }

    override suspend fun getBudgets(): StateResponse<Budgeting?> {
        return try {
            // Make a GET request
            val response: HttpResponse = client.get("http://localhost:5000/get-budgets")
            val data = Json.decodeFromString(Budgeting.serializer(), response.bodyAsText())
//            println("Status: ${response.status} - $data")
            StateResponse.Success(data)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            StateResponse.Error(e)
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }
    override suspend fun changeCategory(id:String,category:String) {
        try {
            val spaceFormattedString = category.replace(" ","+")
            // Make a GET request
            val response: HttpResponse = client.get("http://localhost:5000/update-category?id=$id&newCategory=$spaceFormattedString")
//            println("Status: ${response.status} - $data")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }

    override suspend fun getTransactions() : StateResponse<Transactions?> {
        return try {
            // Make a GET request
            val response: HttpResponse = client.get("http://localhost:5000/get-txns")
            val data = Json.decodeFromString(Transactions.serializer(), response.bodyAsText())
//            println("Status: ${response.status} - $data")
            StateResponse.Success(data)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            StateResponse.Error(e)
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }

    override suspend fun getCategories(): StateResponse<Categories?> {
        return try {
            // Make a GET request
            val response: HttpResponse = client.get("http://localhost:5000/get-categories")
            val data = Json.decodeFromString(Categories.serializer(), response.bodyAsText())
//            println("Status: ${response.status} - $data")
            StateResponse.Success(data)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            StateResponse.Error(e)
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }
}

interface IBudgetService {
    suspend fun refresh(): StateResponse<Boolean>
    suspend fun getBudgets(): StateResponse<Budgeting?>
    suspend fun getTransactions() : StateResponse<Transactions?>
    suspend fun getCategories(): StateResponse<Categories?>
    suspend fun changeCategory(id:String,category:String)
}