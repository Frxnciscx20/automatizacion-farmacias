Feature: Búsqueda de medicamentos en SalcoBrand

  @FarmaciasSalcoBrand @automatizacion-farmacias
  Scenario Outline: Buscar un medicamento y verificar información
    Given que el usuario navega al sitio de SalcoBrand
    When el usuario en Farmacias SalcoBrand busca el medicamento "<medicamento>"
    Then debería ver el nombre del producto en Farmacias SalcoBrand
    And debería ver el precio promocional del producto en SalcoBrand
    And debería ver el precio normal del producto en SalcoBrand
    And guardo la URL de la imagen del medicamento en SalcoBrand
    And guardo la URL actual de la página en Salcobrand
    And se guarda la información del medicamento de SalcoBrand en la base de datos

    Examples:
      | medicamento |
      | Lamictal    |
      | Brevex      |
      | Ozempic     |

