package br.com.alura.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.alura.agencias.domain.Agencia;
import com.alura.agencias.domain.http.SituacaoCadastral;
import com.alura.agencias.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import com.alura.agencias.repository.AgenciaRepository;
import com.alura.agencias.service.AgenciaService;
import com.alura.agencias.service.http.SituacaoCadastralHttpService;

import br.com.alura.utils.AgenciaFixture;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AgenciaServiceTest {

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @jakarta.inject.Inject
    private AgenciaService agenciaService;

    @BeforeEach
    public void setUp() {
        Mockito.doNothing().when(agenciaRepository).persist(Mockito.any(Agencia.class));
    }

    @Test
    public void deveNaoCadastrarQuandoClientRetornarNull() {
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);

        // evita extourar a exception
        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class,
                () -> agenciaService.cadastrar(AgenciaFixture.criarAgencia()));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(AgenciaFixture.criarAgencia());
    }

    @Test
    public void deveNaoCadastrarQuandoClientRetornarAgenciaInativa() {
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.INATIVO));

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class,
                () -> agenciaService.cadastrar(AgenciaFixture.criarAgencia()));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(AgenciaFixture.criarAgencia());
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtiva() {
        var agencia = AgenciaFixture.criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.ATIVO));

        agenciaService.cadastrar(agencia);

        Mockito.verify(agenciaRepository).persist(agencia);
    }
}
