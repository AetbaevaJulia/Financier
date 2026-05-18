package com.example.financier.data.mappers

import android.annotation.SuppressLint
import com.example.financier.data.model.OperationEntity
import com.example.financier.data.model.OperationRequest
import com.example.financier.data.model.OperationResponse
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

fun OperationResponse.toEntity(): OperationEntity = OperationEntity(
    operationId = operationId.toString(),
    statementId = statementId.toString(),
    dateTime = Timestamp.valueOf("$postingDate $time").time,
    authorizationCode = authorizationCode,
    amount = amount,
    currency = currency,
    rawDescription = rawDescription,
    normalizedDescription = normalizedDescription,
    bankCategory = bankCategory,
    merchant = merchant,
    category = category,
    subcategory = subcategory,
    operationType = operationType,
    balanceAfter = balanceAfter,
    classificationSource = classificationSource,
    confidence = confidence
)

fun OperationEntity.toRequest(): OperationRequest = OperationRequest(
    operationId = UUID.fromString(operationId),
    statementId = UUID.fromString(statementId),
    operationDate = SimpleDateFormat.getDateInstance().format(dateTime),
    postingDate = SimpleDateFormat.getDateInstance().format(dateTime),
    time = SimpleDateFormat.getTimeInstance().format(dateTime),
    authorizationCode = authorizationCode,
    amount = amount,
    currency = currency,
    rawDescription = rawDescription,
    normalizedDescription = normalizedDescription,
    bankCategory = bankCategory,
    merchant = merchant,
    category = category,
    subcategory = subcategory,
    operationType = operationType,
    balanceAfter = balanceAfter,
    classificationSource = classificationSource,
    confidence = confidence
)
