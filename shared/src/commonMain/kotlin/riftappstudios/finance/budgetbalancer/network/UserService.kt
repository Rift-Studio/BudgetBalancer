package riftappstudios.finance.budgetbalancer.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class UserService(
    val client: HttpClient
) : IUserService {

    override suspend fun login(email: String, password: String): StateResponse<String> {
        return try {
            val response: HttpResponse = client.post("http://localhost:5000/auth/login") {
                setBody(FormDataContent(Parameters.build {
                    append("email", email)
                    append("password", password)
                }))
            }
            println("Status: ${response.status}")
            StateResponse.Success(response.body())
        } catch (e: Exception) {
            println("Error: ${e.message}")
            StateResponse.Error(e)
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }

    override suspend fun createUser(email: String, password: String): StateResponse<String> {
        return try {
            val response: HttpResponse = client.post("http://localhost:5000/auth/create") {
                setBody(FormDataContent(Parameters.build {
                    append("email", email)
                    append("password", password)
                }))
            }
            println("Status: ${response.status}")
            StateResponse.Success(response.body())
        } catch (e: Exception) {
            println("Error: ${e.message}")
            StateResponse.Error(e)
        } finally {
            // Always close the client to release system resources
//            client.close()
        }
    }
}

interface IUserService {
    suspend fun login(email: String, password: String): StateResponse<String>
    suspend fun createUser(email: String, password: String): StateResponse<String>

}