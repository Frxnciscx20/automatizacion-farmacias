Feature: Búsqueda de medicamentos en Cruz Verde

  @CruzVerde @automatizacion-farmacias
  Scenario Outline: Buscar un medicamento y verificar información
    Given que el usuario navega al sitio de Cruz Verde
    When el usuario en Cruz Verde busca el medicamento "<medicamento>"
    Then debería ver el nombre del producto en Cruz Verde
    And debería ver el precio promocional del producto en Cruz Verde
    And debería ver el precio normal del producto en Cruz Verde
    And guardo la URL de la imagen del medicamento en Cruz Verde
    And guardo la URL actual de la página en Cruz Verde
    And se guarda la información del medicamento Cruz Verde en la base de datos

    Examples:
      |  medicamento                   |
      |  Brevex                        |
      |  Brevex                        |
      |  Ozempic                       |
      |  Ibuprofeno                    |
      |  Valpax                        |
      |  Paltomiel                     |
      |  Clonazepam                    |


