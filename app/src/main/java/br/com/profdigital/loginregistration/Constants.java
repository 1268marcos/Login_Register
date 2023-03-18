package br.com.profdigital.loginregistration;

public final class Constants {

    /**
     * Uma boa opção de nome para essa classe é "Constants". Isso porque ela armazena valores fixos que seriam utilizados por outras classes, e "Constants" é um nome descritivo e fácil de entender. Outra opção seria "ValoresFixos"
     */


    public static final String IP_VALUE = "192.168.0.13";

    /** Nesse exemplo, a classe "Constants" é definida como final, o que significa que ela não pode ser subclasseada. Em seguida, definimos as constantes públicas:

     IP_VALUE: uma string com valor "192.168.0.13" obtido através do CMD com o comando IPCONFIG

     Note que essa constante é definida como públicas e estáticas (static final), o que significa que elas podem ser acessadas por outras classes sem a necessidade de instanciar um objeto da classe Constants (sem o uso da instrução 'new'). Além disso, por ser definida como final, essa constante não pode ser alterada depois de ter sido inicializadas (alocada em memória).
     */

    /**
     * Classes que armazenam constantes devem ser simples e diretas, e geralmente não devem ter dependências externas.
     */

}
