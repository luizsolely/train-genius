# TrainGenius API

RESTful API para gerenciamento de treinos gerados por IA e criados manualmente.

## Tecnologias

* Java 17
* Spring Boot
* Spring Web
* Spring Security com JWT
* MapStruct
* Jackson (JSON)
* JUnit 5
* Mockito

## Como executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/traingenius.git
   cd traingenius
   ```
2. Configure variáveis de ambiente:

   * `GOOGLE_API_KEY` (para acesso ao Gemini)
   * Propriedades do banco de dados em `application.properties`
3. Rode a aplicação:

   ```bash
   mvn spring-boot:run
   ```
4. A API estará disponível em `http://localhost:8080`.

## Autenticação

Todos os endpoints protegidos requerem um header `Authorization: Bearer <token>`. Obtenha seu JWT via endpoint de login.

## Endpoints de Workout

Base: `/api/users/{userId}/workouts`

| Método | Endpoint       | Descrição                                       |
| ------ | -------------- | ----------------------------------------------- |
| POST   | `/generate`    | Gera treino automático via IA e salva           |
| POST   | `/`            | Cria treino manual com JSON de `WorkoutRequest` |
| GET    | `/`            | Lista todos os treinos do usuário               |
| GET    | `/{workoutId}` | Busca treino por ID                             |
| DELETE | `/{workoutId}` | Exclui treino por ID                            |

### Exemplos de uso

Gerar treino via IA:

```bash
curl -X POST http://localhost:8080/api/users/1/workouts/generate \
  -H "Authorization: Bearer $JWT"
```

Criar treino manual:

```bash
curl -X POST http://localhost:8080/api/users/1/workouts \
  -H "Authorization: Bearer $JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Leg Day",
    "description": "Treino de pernas intensivo",
    "trainingDate": "2025-05-25",
    "exercises": ["Squat", "Lunge"]
}'
```

Listar treinos:

```bash
curl http://localhost:8080/api/users/1/workouts \
  -H "Authorization: Bearer $JWT"
```

Buscar treino por ID:

```bash
curl http://localhost:8080/api/users/1/workouts/10 \
  -H "Authorization: Bearer $JWT"
```

Excluir treino:

```bash
curl -X DELETE http://localhost:8080/api/users/1/workouts/10 \
  -H "Authorization: Bearer $JWT"
```
