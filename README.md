\# PrinterAgent



PrinterAgent é um agente Java responsável pela coleta automática de informações de impressoras de rede utilizando SNMP.



O agente será instalado no ambiente do cliente, executará em segundo plano e realizará coletas periódicas das impressoras configuradas.



\## Objetivo



O principal objetivo do sistema é coletar e registrar informações de medição das impressoras, especialmente o contador total de páginas.



Entre os dados previstos estão:



\- número de série;

\- contador total;

\- informações de toner;

\- informações de drum;

\- data e hora da coleta;

\- status da comunicação.



O sistema deverá suportar múltiplas impressoras por cliente.



\## Funcionamento previsto



O PrinterAgent deverá:



1\. iniciar automaticamente com o sistema operacional;

2\. permanecer em execução em segundo plano;

3\. realizar coletas periódicas via SNMP;

4\. registrar as coletas em banco de dados;

5\. realizar uma coleta mensal para aferição;

6\. enviar informações ao sistema central por meio de uma API;

7\. registrar falhas de comunicação;

8\. gerar alertas após falhas consecutivas.



\## Desenvolvimento



Este repositório contém a nova implementação estruturada do PrinterAgent.



Antes desta versão foi desenvolvido um protótipo técnico utilizado para validar:



\- comunicação SNMP;

\- SNMP GET;

\- SNMP GETNEXT;

\- SNMP Walk;

\- Printer-MIB;

\- leitura do número de série;

\- leitura do contador total;

\- leitura de toner;

\- leitura de drum.



As descobertas realizadas no protótipo serão registradas separadamente na documentação técnica.



\## Convenções de código



Todos os identificadores internos do projeto deverão utilizar inglês:



\- classes;

\- métodos;

\- variáveis;

\- pacotes;

\- constantes;

\- propriedades internas.



Textos apresentados ao cliente poderão permanecer em português.



\## Documentação



A documentação do projeto está disponível no diretório:



`docs/`



Documentos iniciais:



\- `requirements.md` — requisitos do sistema;

\- `architecture.md` — arquitetura do sistema (futuro);

\- `snmp-research.md` — resultados da pesquisa e do protótipo SNMP (futuro);

\- `decisions/` — registros de decisões arquiteturais.

