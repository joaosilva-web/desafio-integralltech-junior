import { useState } from 'react'

const SETORES = ['TI', 'MANUTENCAO', 'RH', 'FINANCEIRO']
const PRIORIDADES = ['BAIXA', 'MEDIA', 'ALTA', 'CRITICA']

export default function ChamadoForm({ onSubmit, onClose }) {
  const [form, setForm] = useState({
    titulo: '',
    descricao: '',
    setor: 'TI',
    prioridade: 'MEDIA',
    solicitante: '',
  })
  const [erros, setErros] = useState([])
  const [salvando, setSalvando] = useState(false)

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErros([])
    setSalvando(true)
    try {
      await onSubmit(form)
    } catch (msg) {
      setErros(String(msg).split('\n'))
    } finally {
      setSalvando(false)
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Novo Chamado</h2>
          <button className="btn-fechar" onClick={onClose}>✕</button>
        </div>

        {erros.length > 0 && (
          <ul className="alert alert-erro lista-erros">
            {erros.map((e, i) => <li key={i}>{e}</li>)}
          </ul>
        )}

        <form onSubmit={handleSubmit} className="form">
          <label>
            Título
            <input
              name="titulo"
              value={form.titulo}
              onChange={handleChange}
              placeholder="Mínimo 5 caracteres"
              required
            />
          </label>

          <label>
            Descrição
            <textarea
              name="descricao"
              value={form.descricao}
              onChange={handleChange}
              rows={3}
              placeholder="Descreva o problema"
              required
            />
          </label>

          <div className="form-row">
            <label>
              Setor
              <select name="setor" value={form.setor} onChange={handleChange}>
                {SETORES.map((s) => <option key={s} value={s}>{s}</option>)}
              </select>
            </label>

            <label>
              Prioridade
              <select name="prioridade" value={form.prioridade} onChange={handleChange}>
                {PRIORIDADES.map((p) => <option key={p} value={p}>{p}</option>)}
              </select>
            </label>
          </div>

          <label>
            Solicitante
            <input
              name="solicitante"
              value={form.solicitante}
              onChange={handleChange}
              placeholder="Seu nome"
              required
            />
          </label>

          <div className="form-acoes">
            <button type="button" className="btn btn-secundario" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary" disabled={salvando}>
              {salvando ? 'Salvando...' : 'Abrir Chamado'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
