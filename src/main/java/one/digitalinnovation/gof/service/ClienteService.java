package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.execption.ResourceNotFoundException;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.contract.IClienteService;
import one.digitalinnovation.gof.service.contract.IViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import one.digitalinnovation.gof.model.Cliente;

import java.util.Optional;

@Service
public class ClienteService implements IClienteService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private IViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
    }

    @Override
    public Cliente inserir(ClienteDTO cliente) {
        return salvarClienteComCep(cliente);
    }

    @Override
    public Cliente atualizar(Long id, ClienteDTO cliente) {
        return salvarClienteComCep(cliente);
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }


    private Cliente salvarClienteComCep(ClienteDTO clienteDTO) {

        String cep = clienteDTO.cep().replace("-", "").trim();

        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {

            Endereco novoEndereco = viaCepService.consultarCep(cep);


            novoEndereco.setCep(cep);


            return enderecoRepository.save(novoEndereco);
        });

        Cliente cliente = new Cliente();
        cliente.setEndereco(endereco);
        cliente.setNome(clienteDTO.nome());

        return clienteRepository.save(cliente);
    }

}