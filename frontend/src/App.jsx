import { useState, useEffect, useCallback } from 'react'
import ChamadoForm from './components/ChamadoForm'
import ChamadoList from './components/ChamadoList'
import AnaliseModal from './components/AnaliseModal'
import './App.css'

export default function App() {
  const [chamados, setChamados] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState(null)
  const [mostrarForm, setMostrarForm] = useState(false)
  const [analise, setAnalise] = useState(null)
  const [analisando, setAnalisando] = useState(null)

  const carregarChamados = useCallback(async () => {
    try {
      setLoading(true)
      const res = await fetch('/api/chamados')
      if (!res.ok) throw new Error('Erro ao carregar chamados')
      setChamados(await res.json())
      setErro(null)
    } catch (e) {
      setErro(e.message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    carregarChamados()
  }, [carregarChamados])

  async function handleCriar(dados) {
    const res = await fetch('/api/chamados', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(dados),
    })
    if (!res.ok) {
      const body = await res.json()
      throw body.erros ? body.erros.join('\n') : body.erro
    }
    setMostrarForm(false)
    carregarChamados()
  }

  async function handleAnalisar(id) {
    setAnalisando(id)
    try {
      const res = await fetch(`/api/chamados/${id}/analisar`, { method: 'POST' })
      const body = await res.json()
      if (!res.ok) throw body.erro
      setAnalise(body)
    } catch (e) {
      alert(`Erro na análise: ${e}`)
    } finally {
      setAnalisando(null)
    }
  }

  async function handleCancelar(id) {
    if (!confirm('Cancelar este chamado?')) return
    await fetch(`/api/chamados/${id}`, { method: 'DELETE' })
    carregarChamados()
  }

  return (
    <div className="app">
      <header className="header">
        <div className="header-content">
          <h1>Sistema de Chamados</h1>
          <span className="header-sub">IntegrAllTech</span>
        </div>
        <button className="btn btn-primary" onClick={() => setMostrarForm(true)}>
          + Novo Chamado
        </button>
      </header>

      <main className="main">
        {erro && <div className="alert alert-erro">{erro}</div>}

        {loading ? (
          <div className="loading">Carregando chamados...</div>
        ) : (
          <ChamadoList
            chamados={chamados}
            analisando={analisando}
            onAnalisar={handleAnalisar}
            onCancelar={handleCancelar}
          />
        )}
      </main>

      {mostrarForm && (
        <ChamadoForm
          onSubmit={handleCriar}
          onClose={() => setMostrarForm(false)}
        />
      )}

      {analise && (
        <AnaliseModal
          analise={analise}
          onClose={() => setAnalise(null)}
        />
      )}
    </div>
  )
}
