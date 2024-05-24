import { CLI } from "../cli";

export class Movement {
	public name: string;

	constructor(name: string) {
		this.name = name;
	}

	public printData = () => {
		CLI.print(`\t- ${this.name}`);
	};
}

export class Pokemon {
	public name: string;
	public image_url: string;
	public movements: Movement[] = [];

	constructor({
		name,
		image_url,
		movements,
	}: {
		name: string;
		image_url: string;
		movements: Movement[];
	}) {
		this.name = name;
		this.image_url = image_url;
		this.movements = movements;
	}

	public printData = () => {
		CLI.print(`Name: ${this.name}`);
		CLI.print(`Image URL: ${this.image_url}`);
		CLI.print("Movements:");
		this.movements.forEach((movement) => {
			movement.printData();
		});
	};
}
