package com.framework.android.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * UseCase基类
 * 提供通用的业务逻辑执行框架
 */
abstract class BaseUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * 执行用例
     * @param parameters 输入参数
     * @return 执行结果
     */
    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let { result ->
                    Result.Success(result)
                }
            }
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    /**
     * 子类需要实现的具体业务逻辑
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}

/**
 * 无参数UseCase基类
 */
abstract class BaseUseCaseNoParams<R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute().let { result ->
                    Result.Success(result)
                }
            }
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}

/**
 * Flow形式的UseCase基类
 */
abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(parameters: P): Flow<Result<R>> {
        return execute(parameters)
            .catch { e -> emit(Result.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(parameters: P): Flow<Result<R>>
}

/**
 * 无参数Flow形式的UseCase基类
 */
abstract class FlowUseCaseNoParams<R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(): Flow<Result<R>> {
        return execute()
            .catch { e -> emit(Result.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(): Flow<Result<R>>
}

/**
 * 同步UseCase基类
 * 用于不需要协程的简单业务逻辑
 */
abstract class SyncUseCase<in P, R> {
    operator fun invoke(parameters: P): Result<R> {
        return try {
            Result.Success(execute(parameters))
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}

/**
 * 无参数同步UseCase基类
 */
abstract class SyncUseCaseNoParams<R> {
    operator fun invoke(): Result<R> {
        return try {
            Result.Success(execute())
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(): R
}
