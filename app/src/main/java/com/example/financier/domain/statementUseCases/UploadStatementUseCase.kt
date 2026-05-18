package com.example.financier.domain.statementUseCases

import android.content.ContentResolver
import android.net.Uri
import com.example.financier.data.mappers.toEntity
import com.example.financier.data.model.UploadStatementResponse
import com.example.financier.data.repositories.OperationsDatabaseRepository
import com.example.financier.data.repositories.StatementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException
import javax.inject.Inject

interface UploadStatementUseCase {
    suspend operator fun invoke(uri: Uri, token: String): UploadStatementResponse?
}

class UploadStatementUseCaseImpl @Inject constructor(
    private val repository: StatementRepository,
    private val databaseRepository: OperationsDatabaseRepository,
    private val contentResolver: ContentResolver
): UploadStatementUseCase {
    override suspend fun invoke(uri: Uri, token: String): UploadStatementResponse? {
        // 1. Создаем временный файл и MultipartPart
        return withContext(Dispatchers.IO) {
            val contentResolver = contentResolver
            val mimeType = contentResolver.getType(uri) ?: "application/pdf"
            val fileName = uri.path?.substringAfterLast('/')

            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? = mimeType.toMediaTypeOrNull()

                override fun contentLength(): Long =
                    contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1

                override fun writeTo(sink: BufferedSink) {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        sink.writeAll(inputStream.source())
                    } ?: throw IOException("Cannot open input stream for URI: $uri")
                }
            }

            var file = MultipartBody.Part.createFormData("file", fileName, requestBody)

            // 2. Отправляем файл
            repository.uploadStatement(file, token)

            val statements = repository.getAllStatements(token)

            if (statements != null) {
                UploadStatementResponse(statements[0].statementId, "uploading")
            } else null
        }
    }
}
