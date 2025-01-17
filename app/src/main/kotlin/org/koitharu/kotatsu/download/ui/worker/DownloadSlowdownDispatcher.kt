package org.koitharu.kotatsu.download.ui.worker

import kotlinx.coroutines.delay
import org.koitharu.kotatsu.core.parser.MangaRepository
import org.koitharu.kotatsu.core.parser.RemoteMangaRepository
import org.koitharu.kotatsu.parsers.model.MangaSource

class DownloadSlowdownDispatcher(
	private val mangaRepositoryFactory: MangaRepository.Factory,
	private val defaultDelay: Long,
) {
	private val timeMap = HashMap<MangaSource, Long>()

	suspend fun delay(source: MangaSource) {
		val repo = mangaRepositoryFactory.create(source) as? RemoteMangaRepository ?: return
		if (!repo.isSlowdownEnabled()) {
			return
		}
		val lastRequest = synchronized(timeMap) {
			val res = timeMap[source] ?: 0L
			timeMap[source] = System.currentTimeMillis()
			res
		}
		if (lastRequest != 0L) {
			delay(lastRequest + defaultDelay - System.currentTimeMillis())
		}
	}
}
