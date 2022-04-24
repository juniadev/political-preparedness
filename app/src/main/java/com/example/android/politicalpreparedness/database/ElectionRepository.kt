package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {

    override suspend fun insert(election: Election) = withContext(ioDispatcher) {
        electionDao.insert(election)
    }

    override suspend fun getAllElections(): List<Election> = withContext(ioDispatcher) {
        return@withContext electionDao.getAllElections()
    }

    override suspend fun getElectionById(id: Int): Election? = withContext(ioDispatcher) {
        return@withContext electionDao.getElectionById(id)
    }

    override suspend fun delete(election: Election) = withContext(ioDispatcher) {
        electionDao.delete(election)
    }

    override suspend fun deleteElectionById(id: Int) = withContext(ioDispatcher) {
        electionDao.deleteElectionById(id)
    }

    override suspend fun clearElections() = withContext(ioDispatcher) {
        electionDao.clearElections()
    }
}