package com.alura.agencias.service;

import com.alura.agencias.domain.Agencia;
import com.alura.agencias.domain.http.AgenciaHttp;
import com.alura.agencias.domain.http.SituacaoCadastral;
import com.alura.agencias.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import com.alura.agencias.repository.AgenciaRepository;
import com.alura.agencias.service.http.SituacaoCadastralHttpService;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;

    private final MeterRegistry meterRegistry;

    AgenciaService(AgenciaRepository agenciaRepository, MeterRegistry meterRegistry) {
        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }

    @RestClient
    SituacaoCadastralHttpService situacaoCadastralHttpService;

    public void cadastrar(Agencia agencia) {
        Timer timer = meterRegistry.timer("cadastrar_agencia_delta");

        timer.record(() -> {
            AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
            if (agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
                agenciaRepository.persist(agencia);
                Log.info("A agência com o cnpj " + agencia.getCnpj() + " foi cadastrada com sucesso!");
                meterRegistry.counter("agencia_adicionada_counter").increment();
            } else {
                Log.info("A agência com o cnpj " + agencia.getCnpj() + " NÃO foi cadastrada!");
                meterRegistry.counter("agencia_nao_adicionada_counter").increment();
                throw new AgenciaNaoAtivaOuNaoEncontradaException();
            }
        });
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        agenciaRepository.deleteById(id);
        Log.info("A agência com o ID" + id + " foi DELETADA!");
    }

    public void alterar(Agencia agencia) {
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(),
                agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
        Log.info("A agência com o cnpj " + agencia.getCnpj() + " foialterada.");
    }
}
