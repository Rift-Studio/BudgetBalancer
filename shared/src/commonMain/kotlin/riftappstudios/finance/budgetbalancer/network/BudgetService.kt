package riftappstudios.finance.budgetbalancer.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import riftappstudios.finance.budgetbalancer.data.objects.Budgeting
import riftappstudios.finance.budgetbalancer.data.objects.Transactions
import riftappstudios.finance.budgetbalancer.data.objects.UserSingle

class BudgetService(
    val client: HttpClient,
    val userSingle: UserSingle
) : IBudgetService {

    override suspend fun refresh(): StateResponse<Boolean> {
        return try {
            // Make a GET request
            val response: HttpResponse = client.post("http://localhost:5000/submit-txns") {
                setBody(FormDataContent(Parameters.build {
                    append("user_id", userSingle.user?.userId ?: "")
                }))
            }
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
            val response: HttpResponse = client.post("http://localhost:5000/get-budgets") {
                setBody(FormDataContent(Parameters.build {
                    append("user_id", userSingle.user?.userId ?: "")
                }))
            }
            val data = Json.decodeFromString(Budgeting.serializer(), response.bodyAsText())
            println("Status: ${response.status}")
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
            // Make a GET request
            val response: HttpResponse = client.post("http://localhost:5000/update-category") {
                url {
                    parameters.append("id", id)
                    parameters.append("newCategory",category)
                }
                setBody(FormDataContent(Parameters.build {
                    append("user_id", userSingle.user?.userId ?: "")
                }))
            }
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
            val response: HttpResponse = client.post("http://localhost:5000/get-txns") {
                setBody(FormDataContent(Parameters.build {
                    append("user_id", userSingle.user?.userId ?: "")
                }))
            }
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
}

interface IBudgetService {
    suspend fun refresh(): StateResponse<Boolean>
    suspend fun getBudgets(): StateResponse<Budgeting?>
    suspend fun getTransactions() : StateResponse<Transactions?>
    suspend fun changeCategory(id:String,category:String)
}