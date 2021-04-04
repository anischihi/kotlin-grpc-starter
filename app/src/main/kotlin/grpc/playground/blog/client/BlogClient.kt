package grpc.playground.blog.client

import com.example.blog.BlogServiceGrpcKt
import com.example.blog.GetBlogRequest
import com.example.blog.GetBlogResponse
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.util.concurrent.TimeUnit


class BlogClient(private val channel: ManagedChannel) : Closeable {
  private val stub = BlogServiceGrpcKt.BlogServiceCoroutineStub(channel)

  fun getBlog(request: GetBlogRequest): GetBlogResponse =
      runBlocking { stub.getBlog(request) }


  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}

fun main(args: Array<String>) {
  val port = 50051

  val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()

  val client = BlogClient(channel)

  var response: GetBlogResponse = client.getBlog(GetBlogRequest.newBuilder().setId(2).build())
  println("Received response 1")
  println(response)

}
