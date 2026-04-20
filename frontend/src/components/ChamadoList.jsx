const STATUS_LABEL = {
  ABERTO: 'Aberto',
  EM_ATENDIMENTO: 'Em Atendimento',
  RESOLVIDO: 'Resolvido',
  CANCELADO: 'Cancelado',
}

const PRIORIDADE_LABEL = {
  BAIXA: 'Baixa',
  MEDIA: 'Média',
  ALTA: 'Alta',
  CRITICA: 'Crítica',
}

function formatarData(iso) {
  if (!iso) return '—'
  return new Date(iso).toLocaleString('pt-BR')
}

export default function ChamadoList({ chamados, analisando, onAnalisar, onCancelar }) {
  if (chamados.length === 0) {
    return <p className="vazio">Nenhum chamado encontrado. Crie o primeiro!</p>
  }

  return (
    <div className="tabela-wrapper">
      <table className="tabela">
        <thead>
          <tr>
            <th>#</th>
            <th>Título</th>
            <th>Solicitante</th>
            <th>Setor</th>
            <th>Prioridade</th>
            <th>Status</th>
            <th>Abertura</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {chamados.map((c) => {
            const fechado = c.status === 'CANCELADO' || c.status === 'RESOLVIDO'
            return (
              <tr key={c.id} className={fechado ? 'row-fechado' : ''}>
                <td>{c.id}</td>
                <td className="col-titulo" title={c.descricao}>{c.titulo}</td>
                <td>{c.solicitante}</td>
                <td>{c.setor}</td>
                <td>
                  <span className={`badge prioridade-${c.prioridade.toLowerCase()}`}>
                    {PRIORIDADE_LABEL[c.prioridade]}
                  </span>
                </td>
                <td>
                  <span className={`badge status-${c.status.toLowerCase()}`}>
                    {STATUS_LABEL[c.status]}
                  </span>
                </td>
                <td>{formatarData(c.dataAbertura)}</td>
                <td className="col-acoes">
                  <button
                    className="btn btn-ia"
                    onClick={() => onAnalisar(c.id)}
                    disabled={analisando === c.id}
                    title="Analisar com IA"
                  >
                    {analisando === c.id ? '...' : 'IA'}
                  </button>
                  {!fechado && (
                    <button
                      className="btn btn-cancelar"
                      onClick={() => onCancelar(c.id)}
                      title="Cancelar chamado"
                    >
                      ✕
                    </button>
                  )}
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}
