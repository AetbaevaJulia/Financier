package com.example.financier.data.mappers

import com.example.financier.data.model.OperationEntity
import com.example.financier.data.model.OperationRequest
import com.example.financier.data.model.OperationResponse
import java.util.UUID

fun OperationResponse.toEntity(): OperationEntity = OperationEntity(
    operationId = operationId.toString(),
    statementId = statementId.toString(),
    operationDate = operationDate,
    postingDate = postingDate,
    time = time,
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
    operationDate = operationDate,
    postingDate = postingDate.toString(),
    time = time.toString(),
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
