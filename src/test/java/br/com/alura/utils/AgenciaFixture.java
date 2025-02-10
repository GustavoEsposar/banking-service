package br.com.alura.utils;

import com.alura.agencias.domain.Agencia;
import com.alura.agencias.domain.Endereco;
import com.alura.agencias.domain.http.AgenciaHttp;
import com.alura.agencias.domain.http.SituacaoCadastral;

public class AgenciaFixture {

    public static AgenciaHttp criarAgenciaHttp(SituacaoCadastral status) {
        return new AgenciaHttp("Agencia Teste", "Razao Agencia Teste", "123", status.toString());
    }

    public static Agencia criarAgencia() {
        Endereco endereco = new Endereco(1, "Quadra", "Teste", "Teste", 1);
        return new Agencia(1, "Agencia Teste", "Razao Agencia Teste", "123", endereco);
    }
}
