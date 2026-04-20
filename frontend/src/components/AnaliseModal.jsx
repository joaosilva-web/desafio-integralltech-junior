const PRIORIDADE_LABEL = {
  BAIXA: 'Baixa',
  MEDIA: 'Média',
  ALTA: 'Alta',
  CRITICA: 'Crítica',
}

function formatarData(iso) {
  return new Date(iso).toLocaleString('pt-BR')
}

export default function AnaliseModal({ analise, onClose }) {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal modal-analise" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Análise por IA — Chamado #{analise.chamadoId}</h2>
          <button className="btn-fechar" onClick={onClose}>✕</button>
        </div>

        <div className="analise-corpo">
          <div className="analise-grid">
            <div className="analise-item">
              <span className="analise-label">Prioridade sugerida</span>
              <span className={`badge prioridade-${analise.prioridadeSugerida.toLowerCase()} badge-lg`}>
                {PRIORIDADE_LABEL[analise.prioridadeSugerida]}
              </span>
            </div>

            <div className="analise-item">
              <span className="analise-label">Setor sugerido</span>
              <span className="badge badge-setor badge-lg">{analise.setorSugerido}</span>
            </div>
          </div>

          <div className="analise-resumo">
            <span className="analise-label">Resumo da IA</span>
            <p>{analise.resumo}</p>
          </div>

          <div className="analise-rodape">
            Analisado em {formatarData(analise.analisadoEm)}
          </div>
        </div>
      </div>
    </div>
  )
}
