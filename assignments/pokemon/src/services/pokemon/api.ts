import { MovementFetch, PokemonFetch } from "../../types/pokemon-api";

export class PokemonAPI {
	private baseUrl = "https://pokeapi.co/api/v2";

	private buildUrl = (path: string) => `${this.baseUrl}/${path}`;

	private doBasicRequest = async (path: string) => {
		try {
			const res = await fetch(this.buildUrl(path));
			return await res.json();
		} catch (err) {
			return Promise.reject(err);
		}
	};

	public fetchPokemon = async (pokemon: string): Promise<PokemonFetch> =>
		this.doBasicRequest(`pokemon/${pokemon}`);

	public fetchMovement = (movement: string): Promise<MovementFetch> =>
		this.doBasicRequest(`move/${movement}`);
}
