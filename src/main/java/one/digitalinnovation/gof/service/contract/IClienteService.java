package one.digitalinnovation.gof.service.contract;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.model.Cliente;
import org.springframework.data.crossstore.ChangeSetPersister;

public interface IClienteService {

    Iterable<Cliente> buscarTodos();

    Cliente buscarPorId(Long id);

    Cliente inserir(ClienteDTO cliente);

    Cliente atualizar(Long id, ClienteDTO cliente) throws ChangeSetPersister.NotFoundException;

    void deletar(Long id);

}
