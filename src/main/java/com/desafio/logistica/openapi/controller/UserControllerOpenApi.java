package com.desafio.logistica.openapi.controller;

import com.desafio.logistica.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "User", description = "Operações sobre usuários")
public interface UserControllerOpenApi {

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Arquivo processado com sucesso e dados salvos.",
                    content = @Content(
                            schema = @Schema(implementation = Void.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Formato inválido ou erro ao ler o arquivo.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-27T12:00:00",
                  "status": 400,
                  "error": "BAD_REQUEST",
                  "message": "Error reading file: nome_do_arquivo",
                  "path": "/api/v1/user/upload"
                }
                """)
                    )
            ),

            @ApiResponse(
                    responseCode = "413",
                    description = "Tamanho do arquivo excede o limite.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-27T12:00:00",
                  "status": 413,
                  "error": "PAYLOAD_TOO_LARGE",
                  "message": "Maximum upload size exceeded.",
                  "path": "/api/v1/user/upload"
                }
                """)
                    )
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno ao processar o arquivo.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-27T12:00:00",
                  "status": 500,
                  "error": "ERROR_500",
                  "message": "System error. Please contact your service administrator.",
                  "path": "/api/v1/user/upload"
                }
                """)
                    )
            )
    })
    void upload(
            @Parameter(
                    description = "Arquivo .txt contendo os dados desnormalizados dos usuários",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            MultipartFile file
    );

    @Operation(summary = "Consulta usuários, pedidos e produtos, com filtros opcionais por ID do pedido e data")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                [
                  {
                    "userId": 1,
                    "name": "Alice",
                    "orders": [
                      {
                        "orderId": 10,
                        "total": 150.50,
                        "date": "2024-01-10T00:00:00",
                        "products": [
                          {
                            "productId": 100,
                            "value": 150.50
                          }
                        ]
                      }
                    ]
                  }
                ]
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros inválidos (ex: data inválida ou orderId nulo).",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-27T12:00:00",
                  "status": 400,
                  "error": "BAD_REQUEST",
                  "message": "Order ID must not be null.",
                  "path": "/api/v1/user/find"
                }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado para o filtro informado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-27T12:00:00",
                  "status": 404,
                  "error": "NOT_FOUND",
                  "message": "Order with ID 999 not found.",
                  "path": "/api/v1/user/find"
                }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno ao processar a requisição.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-27T12:00:00",
                  "status": 500,
                  "error": "ERROR_500",
                  "message": "System error. Please contact your service administrator.",
                  "path": "/api/v1/user/find"
                }
                """)
                    )
            )
    })
    ResponseEntity<List<UserDto>> findAllOrFilter(
            @Parameter(description = "ID do pedido", example = "10") Integer orderId,
            @Parameter(description = "Data inicial (yyyy-MM-dd)", example = "2021-01-01") LocalDate dateStart,
            @Parameter(description = "Data final (yyyy-MM-dd)", example = "2021-12-31") LocalDate endDate
    );
}