export type Tasks = {
  id: number;
  title: string;
  description: string;
  status: "TODO" | "IN_PROGRESS" | "DONE" ;
  dueDate: string;
  responsible?: number;
};
