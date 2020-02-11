import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IVideogame {
  id?: number;
  title?: string;
  description?: string;
  imageUrl?: string;
  repositoryUrl?: string;
  deployUrl?: string;
  releaseDate?: Moment;
  isSearchingCollab?: boolean;
  developers?: IUser[];
}

export class Videogame implements IVideogame {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public imageUrl?: string,
    public repositoryUrl?: string,
    public deployUrl?: string,
    public releaseDate?: Moment,
    public isSearchingCollab?: boolean,
    public developers?: IUser[]
  ) {
    this.isSearchingCollab = this.isSearchingCollab || false;
  }
}
