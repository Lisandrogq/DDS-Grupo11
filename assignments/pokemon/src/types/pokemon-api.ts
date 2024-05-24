interface NamedWithURL {
	name: string;
	url: string;
}

interface PokemonAbility {
	ability: NamedWithURL;
	is_hidden: boolean;
}

interface PokemonMove {
	move: NamedWithURL;
}

interface PokemonStat {
	stat: NamedWithURL;
	base_stat: number;
}

interface PokemonType {
	type: NamedWithURL;
}

interface PokemonSprites {
	front_default: string | null;
	front_shiny: string | null;
	back_default: string | null;
	back_shiny: string | null;
}

export interface PokemonFetch {
	id: number;
	name: string;
	abilities: PokemonAbility[];
	moves: PokemonMove[];
	stats: PokemonStat[];
	types: PokemonType[];
	sprites: PokemonSprites;
}

export interface MovementFetch {
	id: number;
	name: string;
	type: NamedWithURL;
	power: number | null;
	pp: number;
	accuracy: number | null;
	learned_by_pokemon: NamedWithURL[];
}
