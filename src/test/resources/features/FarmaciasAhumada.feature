Feature: Búsqueda de medicamentos en Farmacias Ahumada

  @FarmaciasAhumada @automatizacion-farmacias
  Scenario Outline: Buscar un medicamento y verificar información
    Given que el usuario navega al sitio de Farmacias Ahumada
    When el usuario en Farmacias Ahumada busca el medicamento "<medicamento>"
    Then debería ver el nombre del producto en Farmacias Ahumada
    And debería ver el precio promocional del producto en Farmacias Ahumada
    And debería ver el precio normal del producto en Farmacias Ahumada
    And guardo la URL de la imagen del medicamento en Farmacias Ahumada
    And guardo la URL actual de la página en farmacias Ahumada
    And se guarda la información del medicamento en la base de datos

    Examples:
      |  medicamento                         |
      |  Lamictal                            |
      |  Brevex                              |
      |  Ozempic                             |
      |  Valpax                              |
      |  Paltomiel Pediátrico                |
      |  Melatonina 3 mg x 60 Capsulas       |
      |  Clonazepam 2 mg Caja 30 Comp. CHILE |
