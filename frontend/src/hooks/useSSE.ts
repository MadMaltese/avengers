import { useEffect, useRef, useCallback } from 'react'
import { useAuthStore } from '../stores/authStore'

export function useSSE(url: string, onMessage: (event: MessageEvent) => void) {
  const eventSourceRef = useRef<EventSource | null>(null)
  const token = useAuthStore(s => s.token)

  const connect = useCallback(() => {
    if (!token) return
    const es = new EventSource(`${url}?token=${token}`)
    es.onmessage = onMessage
    es.onerror = () => {
      es.close()
      setTimeout(connect, 3000)
    }
    eventSourceRef.current = es
  }, [url, token, onMessage])

  useEffect(() => {
    connect()
    return () => { eventSourceRef.current?.close() }
  }, [connect])
}
