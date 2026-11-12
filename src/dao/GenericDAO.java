package dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    T inserir(T entidade);

    Optional<T> buscarPorId(int id);

    List<T> listarTodos();

    boolean atualizar(T entidade);

    boolean excluir(int id);
}
